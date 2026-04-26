package edu.psu.ist.registration.controller;

import edu.psu.ist.registration.dao.RegistrationRepository;
import edu.psu.ist.registration.entity.Member;
import edu.psu.ist.registration.entity.Registration;
import edu.psu.ist.registration.service.EventService;
import edu.psu.ist.registration.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    private MemberService memberService;
    private EventService eventService;
    private RegistrationRepository registrationRepository;

    @Autowired
    public ExportController(MemberService theMemberService,
                            EventService theEventService,
                            RegistrationRepository theRegistrationRepository) {
        memberService = theMemberService;
        eventService = theEventService;
        registrationRepository = theRegistrationRepository;
    }

    // ─────────────────────────────────────────
    // Export ALL members
    // ─────────────────────────────────────────
    @GetMapping("/members")
    public void exportAllMembers(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=all-members.xlsx");

        List<Member> members = memberService.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("All Members");

            // Header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Header row
            String[] headers = {
                "Member ID", "First Name", "Last Name", "Username", "Email",
                "Phone", "Best Game", "Age Range", "Address", "City", "State",
                "Zip Code", "Gaming System", "Attended Before", "Join Tournament",
                "College Student", "Interested in PSU", "Grade/Year",
                "Guardian Name", "Guardian Phone", "How Heard", "Accepted Terms"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (Member m : members) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(m.getMemberId());
                row.createCell(1).setCellValue(nullSafe(m.getFirstName()));
                row.createCell(2).setCellValue(nullSafe(m.getLastName()));
                row.createCell(3).setCellValue(nullSafe(m.getUserName()));
                row.createCell(4).setCellValue(nullSafe(m.getEmail()));
                row.createCell(5).setCellValue(nullSafe(m.getPhoneNumber()));
                row.createCell(6).setCellValue(nullSafe(m.getBestGame()));
                row.createCell(7).setCellValue(nullSafe(m.getAgeRange()));
                row.createCell(8).setCellValue(nullSafe(m.getAddress()));
                row.createCell(9).setCellValue(nullSafe(m.getCity()));
                row.createCell(10).setCellValue(nullSafe(m.getState()));
                row.createCell(11).setCellValue(nullSafe(m.getZipCode()));
                row.createCell(12).setCellValue(nullSafe(m.getGamingSystem()));
                row.createCell(13).setCellValue(nullSafe(m.getAttendedBefore()));
                row.createCell(14).setCellValue(nullSafe(m.getJoinTournament()));
                row.createCell(15).setCellValue(nullSafe(m.getCollegeStudent()));
                row.createCell(16).setCellValue(nullSafe(m.getInterestedInPSU()));
                row.createCell(17).setCellValue(nullSafe(m.getGradeYear()));
                row.createCell(18).setCellValue(nullSafe(m.getGuardianName()));
                row.createCell(19).setCellValue(nullSafe(m.getGuardianPhone()));
                row.createCell(20).setCellValue(nullSafe(m.getHowHeard()));
                row.createCell(21).setCellValue(m.isAcceptedTerms() ? "Yes" : "No");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
        }
    }

    // ─────────────────────────────────────────
    // Export members registered for a specific event
    // ─────────────────────────────────────────
    @GetMapping("/event")
    public void exportEventRegistrations(@RequestParam("eventId") int eventId,
                                         HttpServletResponse response) throws IOException {

        String eventName = "event";
        try {
            eventName = eventService.findById(eventId).getEventName()
                    .replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
        } catch (Exception ignored) {}

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=registrations-" + eventName + ".xlsx");

        List<Registration> registrations =
                registrationRepository.findByEvent_EventIdOrderByRegistrationDateAsc(eventId);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Event Registrations");

            CellStyle headerStyle = createHeaderStyle(workbook);

            // Header row
            String[] headers = {
                "Registration ID", "Registration Date", "First Name", "Last Name",
                "Email", "Phone", "Age Range", "Gaming System",
                "Join Tournament", "Guardian Name", "Guardian Phone",
                "Paid", "Checked In"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (Registration reg : registrations) {
                Member m = reg.getMember();
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reg.getRegistrationId());
                row.createCell(1).setCellValue(
                        reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString() : "");
                row.createCell(2).setCellValue(m != null ? nullSafe(m.getFirstName()) : "");
                row.createCell(3).setCellValue(m != null ? nullSafe(m.getLastName()) : "");
                row.createCell(4).setCellValue(m != null ? nullSafe(m.getEmail()) : "");
                row.createCell(5).setCellValue(m != null ? nullSafe(m.getPhoneNumber()) : "");
                row.createCell(6).setCellValue(m != null ? nullSafe(m.getAgeRange()) : "");
                row.createCell(7).setCellValue(m != null ? nullSafe(m.getGamingSystem()) : "");
                row.createCell(8).setCellValue(m != null ? nullSafe(m.getJoinTournament()) : "");
                row.createCell(9).setCellValue(m != null ? nullSafe(m.getGuardianName()) : "");
                row.createCell(10).setCellValue(m != null ? nullSafe(m.getGuardianPhone()) : "");
                row.createCell(11).setCellValue(reg.isPaid() ? "Yes" : "No");
                row.createCell(12).setCellValue(reg.isCheckedIn() ? "Yes" : "No");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
        }
    }

    // ─────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────
    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
}
