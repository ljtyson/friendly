import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

public class Example {
	
	public static void main() {
		File file = new File("input.txt");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line = reader.readLine();
			while (line != null) {
				String[] data = line.split("\t", -1);
				insertRecord(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
						data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18],
						data[19], data[20], data[21], data[22], data[23], data[24], data[25], data[26], data[27]);
				line = reader.readLine();
			}
			createScriptOutput(exportData());
		} catch (Exception e) {
			;
		}
	}

	private static void createScriptOutput(ByteArrayOutputStream bos) throws IOException {
		String outputFileName = "Data Analysis.xls";
		InputStream is = new ByteArrayInputStream(bos.toByteArray());
		File outputFile = new File(outputFileName, "xls");
	}

	private static void insertRecord(String ssn, String type, String agencycode, String soi, String son,
			String lastname, String firstname, String middle, String suffix, String dob, String pobcity,
			String pobcounty, String pobstate, String pobcountry, String closedcasecount, String casenumber,
			String casetype, String inputform, String formdate, String eqiprequest, String nbiscaseid,
			String enrolldate, String unenrolldate, String enrollstatus, String enrollreason, String adjudication,
			String errormessage, String additionalcomments) {
		String sql = String.format("insert into EPA_5805 ( SSN, TYPE, AGENCYCODE, SOI, SON, LASTNAME, FIRSTNAME, "
				+ " MIDDLE, SUFFIX, DOB, POB_CITY, POB_COUNTY, POB_STATE, POB_COUNTRY, CLOSED_CASE_COUNT, "
				+ " CASE_NUMBER, CASE_TYPE, INPUT_FORM, FORM_DATE, EQIP_REQUEST, NBIS_CASE_ID, ENROLL_DATE, "
				+ " UNENROLL_DATE, ENROLL_STATUS, ENROLL_REASON, ADJUDICATION, ERROR_MESSAGE, ADDITIONAL_COMMENT ) "
				+ " values ( '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', "
				+ "  '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s' ) ",
				StringUtils.isNotBlank(ssn) ? ssn : null, StringUtils.isNotBlank(type) ? type : null,
				StringUtils.isNotBlank(agencycode) ? agencycode : null, StringUtils.isNotBlank(soi) ? soi : null,
				StringUtils.isNotBlank(son) ? son : null, StringUtils.isNotBlank(lastname) ? lastname : null,
				StringUtils.isNotBlank(firstname) ? firstname : null, StringUtils.isNotBlank(middle) ? middle : null,
				StringUtils.isNotBlank(suffix) ? suffix : null, StringUtils.isNotBlank(dob) ? dob : null,
				StringUtils.isNotBlank(pobcity) ? pobcity : null, StringUtils.isNotBlank(pobcounty) ? pobcounty : null,
				StringUtils.isNotBlank(pobstate) ? pobstate : null,
				StringUtils.isNotBlank(pobcountry) ? pobcountry : null,
				StringUtils.isNotBlank(closedcasecount) ? closedcasecount : null,
				StringUtils.isNotBlank(casenumber) ? casenumber : null,
				StringUtils.isNotBlank(casetype) ? casetype : null,
				StringUtils.isNotBlank(inputform) ? inputform : null,
				StringUtils.isNotBlank(formdate) ? formdate : null,
				StringUtils.isNotBlank(eqiprequest) ? eqiprequest : null,
				StringUtils.isNotBlank(nbiscaseid) ? nbiscaseid : null,
				StringUtils.isNotBlank(enrolldate) ? enrolldate : null,
				StringUtils.isNotBlank(unenrolldate) ? unenrolldate : null,
				StringUtils.isNotBlank(enrollstatus) ? enrollstatus : null,
				StringUtils.isNotBlank(enrollreason) ? enrollreason : null,
				StringUtils.isNotBlank(adjudication) ? adjudication : null,
				StringUtils.isNotBlank(errormessage) ? errormessage : null,
				StringUtils.isNotBlank(additionalcomments) ? additionalcomments : null);
		sql = sql.replace("'null'", "null");
		/* etk.createSQL(sql).execute(); */ }

	private static List<Map<String, Object>> getData() {
		String sql = "select x.*, p.C_FIRST_NAME as PSS_FIRST_NAME, p.C_LAST_NAME as PSS_LAST_NAME, "
				+ "       p.C_EPA_EMAIL_ADDRESS as PSS_EPA_EMAIL_ADDRESS, "
				+ "       uis.c_name as PSS_USACCESS_ISSUANCE_STATUS, "
				+ "       latest_case.caseStatus as PSS_CASE_STATUS, "
				+ "       latest_case.workflowState as PSS_CASE_WORKFLOW_STATUS, "
				+ "       latest_case.cto as PSS_CLEARED_TO_ONBOARD, "
				+ "       latest_case.ctoDate as PSS_CLEARED_TO_ONBOARD_DATE, "
				+ "       NVL(p.C_SEPARATION_DATE, p.c_not_hired_date) as PSS_TERMINATION_DATE " + "from EPA_5805 x "
				+ "     LEFT JOIN T_PERSON p on replace(p.C_SOCIAL_SECURITY_NUMBER, '-','') = x.ssn "
				+ "     LEFT JOIN (select * " + "           from (select cse.ID CID, cse.id_parent as pid, "
				+ "                        st.c_name as caseStatus, "
				+ "                        rf.c_name as workflowState, "
				+ "                        case when C_CLEARED_TO_ONBOARD = 1 then 'Yes' else 'No' end as cto, "
				+ "                        to_char(cse.C_CLEARED_TO_ONBOARD_DATE, 'MM/DD/YYYY') as ctoDate, "
				+ "                        rank() over (partition by cse.id_parent "
				+ "                            order by " + "                                case "
				+ "                                    when st.C_CODE = 'caseStatusInProgress' then 1 "
				+ "                                    when st.C_CODE = 'caseStatusComplete' then 2 "
				+ "                                    end, cse.id desc) as rnk " + "                 from t_case cse "
				+ "                          JOIN T_CASE_STATUS st ON st.id = cse.C_CASE_STATUS "
				+ "                          JOIN T_APPLICANT_TYPE at on at.id = cse.C_APPLICANT_TYPE "
				+ "                          JOIN T_FED_OR_NON_FED_TYPE FONF ON FONF.ID = AT.C_METRICS_REPORT_DESIGNATION "
				+ "                          JOIN T_DUTY_LOCATION dl on dl.id = cse.C_DUTY_LOCATION "
				+ "                          JOIN T_RF_STATE rf on rf.id = cse.c_status "
				+ "                          JOIN T_CE_PRODUCT cep on cep.id = cse.C_CE_PRODUCT "
				+ "                          JOIN T_RISK_LEVEL rl on rl.id = cse.C_RISK_LEVEL "
				+ "                          JOIN T_SENSITIVITY s on s.id = cse.C_SENSITIVITY_LEVEL "
				+ "                          JOIN T_INVESTIGATION_TYPE it on it.id = cse.C_INVESTIGATION_TYPE "
				+ "                ) " + "           where rnk = 1 ) latest_case on latest_case.pid = p.id "
				+ "     LEFT JOIN T_USACCESS_HISTORY uh on uh.id_parent = p.id "
				+ "     LEFT JOIN T_USACCESS_ISSUANCE_STATUS uis on uis.id = uh.C_ISSUANCE_STATUS "
				+ "where (uh.id is null or uh.id = (select max(id) from T_USACCESS_HISTORY where id_parent = p.id)) ";
		return new ArrayList();
		/* return createSQL(sql).fetchList(); */ 
		}

	public static ByteArrayOutputStream exportData() throws Exception {
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook();
			HSSFSheet rawData = workbook.createSheet("Data");
			writeData(rawData, getData());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			return bos;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private static void writeData(HSSFSheet sheet, List<Map<String, Object>> data) {
		AtomicInteger rowNum = new AtomicInteger(0);
		AtomicInteger numCols = new AtomicInteger(0);
		data.forEach(row -> {
			AtomicInteger colNum = new AtomicInteger();
			if (Objects.equals(rowNum.get(), 0)) {
				numCols.set(row.size());
				Row headerRow = sheet.createRow(rowNum.getAndIncrement());
				row.forEach((key, value) -> {
					Cell headerCell = headerRow.createCell(colNum.getAndIncrement());
					headerCell.setCellValue(key);
				});
			}
			Row dataRow = sheet.createRow(rowNum.getAndIncrement());
			colNum.set(0);
			row.forEach((key, value) -> {
				Cell dataCell = dataRow.createCell(colNum.getAndIncrement());
				if (value instanceof java.sql.Timestamp) {
					LocalDate dateValue = ((Timestamp) value).toLocalDateTime().toLocalDate();
					String dateString = dateValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					HSSFRichTextString a = new HSSFRichTextString(dateString);
					dataCell.setCellValue(a);
				} else if (!Objects.isNull(value)) {
					dataCell.setCellValue((String) value);
				} else {
					dataCell.setCellValue("");
				}
			});
		});
		sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, numCols.get() - 1));
		sheet.createFreezePane(0, 1);
		for (int x = 0; x < numCols.get(); x++) {
			sheet.autoSizeColumn(x);
		}
	}
}