package edu.psu.ist.registration.controller;

import edu.psu.ist.registration.dao.RegistrationRepository;
import edu.psu.ist.registration.entity.Event;
import edu.psu.ist.registration.entity.Member;
import edu.psu.ist.registration.entity.Registration;
import edu.psu.ist.registration.dao.MemberRepository;
import edu.psu.ist.registration.service.EventService;
import edu.psu.ist.registration.service.MemberService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/import")
public class ImportController {

    private MemberService memberService;
    private MemberRepository memberRepository;
    private EventService eventService;
    private RegistrationRepository registrationRepository;

    @Autowired
    public ImportController(MemberService theMemberService,
                            MemberRepository theMemberRepository,
                            EventService theEventService,
                            RegistrationRepository theRegistrationRepository) {
        memberService = theMemberService;
        memberRepository = theMemberRepository;
        eventService = theEventService;
        registrationRepository = theRegistrationRepository;
    }

    // IMPORT MEMBERS ONLY
    @PostMapping("/members")
    public String importMembers(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("importError", "Please select a file.");
            return "redirect:/members/list";
        }
        try {
            List<Member> parsed = parseFile(file);
            int saved = 0;
            for (Member m : parsed) {
                try {
                    memberService.save(m);
                    saved++;
                } catch (Exception ignored) {}
            }
            redirectAttributes.addFlashAttribute("importSuccess",
                    "Processed " + saved + " member(s). Duplicate emails were updated, not duplicated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("importError", "Failed to parse file: " + e.getMessage());
        }
        return "redirect:/members/list";
    }

    // IMPORT & REGISTER FOR A SPECIFIC EVENT
    @PostMapping("/event-members")
    public String importForEvent(@RequestParam("file") MultipartFile file,
                                 @RequestParam("eventId") int eventId,
                                 @RequestParam(value = "redirectTo", defaultValue = "list") String redirectTo,
                                 RedirectAttributes redirectAttributes) {

        String redirectUrl = redirectTo.equals("view")
                ? "redirect:/events/view?eventId=" + eventId
                : "redirect:/events/list";

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("importError", "Please select a file.");
            return redirectUrl;
        }

        Event targetEvent;
        try {
            targetEvent = eventService.findById(eventId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("importError", "Event not found.");
            return redirectUrl;
        }

        try {
            List<Member> parsed = parseFile(file);
            int registered = 0, skippedAlreadyReg = 0, skippedFull = 0;

            for (Member m : parsed) {
                int currentCount = registrationRepository.countByEvent_EventId(targetEvent.getEventId());
                if (currentCount >= targetEvent.getCapacity()) {
                    skippedFull++;
                    continue;
                }

                Member savedMember = memberService.save(m);

                Optional<Registration> existingReg = registrationRepository
                        .findByMember_MemberIdAndEvent_EventId(
                                savedMember.getMemberId(), targetEvent.getEventId());
                if (existingReg.isPresent()) {
                    skippedAlreadyReg++;
                    continue;
                }

                try {
                    registrationRepository.save(new Registration(savedMember, targetEvent));
                    registered++;
                } catch (Exception ignored) {}
            }

            String msg = "Registered " + registered + " member(s) for " + targetEvent.getEventName() + ".";
            if (skippedAlreadyReg > 0) msg += " " + skippedAlreadyReg + " already registered.";
            if (skippedFull > 0)       msg += " " + skippedFull + " skipped — event full.";
            redirectAttributes.addFlashAttribute("importSuccess", msg);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("importError", "Failed to parse file: " + e.getMessage());
        }
        return redirectUrl;
    }

    // FILE ROUTING
    private List<Member> parseFile(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if (name == null) throw new Exception("Invalid filename.");
        if (name.endsWith(".xlsx")) return parseExcel(file, false);
        if (name.endsWith(".xls"))  return parseExcel(file, true);
        if (name.endsWith(".csv"))  return parseCsv(file);
        throw new Exception("Unsupported file type. Use .xlsx, .xls, or .csv.");
    }

    // EXCEL PARSER
    private List<Member> parseExcel(MultipartFile file, boolean legacy) throws Exception {
        List<Member> members = new ArrayList<>();

        ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());

        Workbook wb = legacy
                ? new HSSFWorkbook(stream)
                : new XSSFWorkbook(stream);

        try (wb) {
            Sheet sheet = wb.getSheetAt(0);
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) { firstRow = false; continue; }
                if (isRowEmpty(row)) continue;
                Member m = rowToMember(row);
                if (m.getFirstName() == null && m.getEmail() == null) continue;
                members.add(m);
            }
        }
        return members;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String val = cell.toString().trim();
                if (!val.isEmpty()) return false;
            }
        }
        return true;
    }

    private Member rowToMember(Row row) {
        Member m = new Member();
        m.setFirstName(getCellString(row, 0));
        m.setLastName(getCellString(row, 1));
        m.setUserName(getCellString(row, 2));
        m.setEmail(getCellString(row, 3));
        m.setPhoneNumber(getCellString(row, 4));
        m.setBestGame(getCellString(row, 5));
        m.setAddress(getCellString(row, 6));
        m.setCity(getCellString(row, 7));
        m.setState(getCellString(row, 8));
        m.setZipCode(getCellString(row, 9));
        m.setAgeRange(getCellString(row, 10));
        m.setGuardianName(getCellString(row, 11));
        m.setGuardianPhone(getCellString(row, 12));
        m.setCollegeStudent(getCellString(row, 13));
        m.setInterestedInPSU(getCellString(row, 14));
        m.setGradeYear(getCellString(row, 15));
        m.setGamingSystem(getCellString(row, 16));
        m.setAttendedBefore(getCellString(row, 17));
        m.setJoinTournament(getCellString(row, 18));
        m.setHowHeard(getCellString(row, 19));
        return m;
    }

    // Handles all cell types without using deprecated setCellType
    private String getCellString(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        String val;
        switch (cell.getCellType()) {
            case STRING:
                val = cell.getStringCellValue();
                break;
            case NUMERIC:
                val = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                if (val.endsWith(".0")) val = val.substring(0, val.length() - 2);
                break;
            case BOOLEAN:
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                try {
                    val = cell.getStringCellValue();
                } catch (Exception e) {
                    val = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                    if (val.endsWith(".0")) val = val.substring(0, val.length() - 2);
                }
                break;
            default:
                return null;
        }
        return (val == null || val.isBlank()) ? null : val.trim();
    }

    // CSV PARSER
    private List<Member> parseCsv(MultipartFile file) throws Exception {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                if (line.isBlank()) continue;
                String[] cols = parseCsvLine(line);
                Member m = new Member();
                m.setFirstName(getCol(cols, 0));
                m.setLastName(getCol(cols, 1));
                m.setUserName(getCol(cols, 2));
                m.setEmail(getCol(cols, 3));
                m.setPhoneNumber(getCol(cols, 4));
                m.setBestGame(getCol(cols, 5));
                m.setAddress(getCol(cols, 6));
                m.setCity(getCol(cols, 7));
                m.setState(getCol(cols, 8));
                m.setZipCode(getCol(cols, 9));
                m.setAgeRange(getCol(cols, 10));
                m.setGuardianName(getCol(cols, 11));
                m.setGuardianPhone(getCol(cols, 12));
                m.setCollegeStudent(getCol(cols, 13));
                m.setInterestedInPSU(getCol(cols, 14));
                m.setGradeYear(getCol(cols, 15));
                m.setGamingSystem(getCol(cols, 16));
                m.setAttendedBefore(getCol(cols, 17));
                m.setJoinTournament(getCol(cols, 18));
                m.setHowHeard(getCol(cols, 19));
                if (m.getFirstName() == null && m.getEmail() == null) continue;
                members.add(m);
            }
        }
        return members;
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') { inQuotes = !inQuotes; }
            else if (c == ',' && !inQuotes) { result.add(sb.toString().trim()); sb.setLength(0); }
            else { sb.append(c); }
        }
        result.add(sb.toString().trim());
        return result.toArray(new String[0]);
    }

    private String getCol(String[] cols, int i) {
        if (cols == null || i >= cols.length) return null;
        String v = cols[i];
        return (v == null || v.isBlank()) ? null : v.trim();
    }
}
