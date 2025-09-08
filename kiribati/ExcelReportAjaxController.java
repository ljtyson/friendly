package com.epa.dashboard.reports.excel;

import com.entellitrak.ApplicationException;
import com.entellitrak.PageExecutionContext;
import com.entellitrak.page.PageController;
import com.entellitrak.page.Response;
import com.epa.utilities.CommonUtilities;
import java.util.Objects;

public class ExcelReportAjaxController implements PageController {
	@Override
	public Response execute(PageExecutionContext etk) throws ApplicationException {
		String report = etk.getParameters().getSingle("report");
		if (Objects.isNull(report)) {
			throw new ApplicationException("Report parameter required");
		}
		try {
			switch (report) {
				case "federalOnboardingStatusReport":
					String types = etk.getParameters().getSingle("types");
					String startDate = etk.getParameters().getSingle("startDate");
					String endDate = etk.getParameters().getSingle("endDate");
					Long roleId = etk.getCurrentUser().getRole().getId();
					Long userId = etk.getCurrentUser().getId();
					return FederalOnboardingStatusReport.exportToExcel(etk, userId, roleId, startDate, endDate, types);
				case "cvTrackingReport":
					return CvTrackingReport.exportToExcel(etk);
				case "activeSCIListingReport":
					return activeSCIListingReport.exportToExcel(etk);
				case "NSITrainingsReport":
					return NSITrainingsReport.exportToExcel(etk);
				case "separationreport":
					return separationReport.exportToExcel(etk);
				default:
					throw new ApplicationException("Invalid report");
			}
		} catch (Exception e) {
			throw new ApplicationException("An error has occurred: " + e.getMessage());
		}
	}
}