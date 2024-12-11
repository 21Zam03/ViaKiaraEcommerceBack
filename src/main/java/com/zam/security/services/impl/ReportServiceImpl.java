package com.zam.security.services.impl;

import com.zam.security.services.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public File generateReport(String saleId, String date, Double totalPrice,
                               String client, String productQuantity) throws JRException, IOException {
        String destinationPath = "src" + File.separator +
                                "main" + File.separator +
                                "resources" + File.separator +
                                "static" + File.separator +
                                "ReportGenerated.pdf";
        String filePath = "src" + File.separator +
                            "main" + File.separator +
                            "resources" + File.separator +
                            "templates" + File.separator +
                            "report" + File.separator +
                            "Report.jrxml";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sale_id", saleId);
        parameters.put("date", date);
        parameters.put("total_price", totalPrice);
        parameters.put("client", client);
        parameters.put("product_quantity", productQuantity);
        parameters.put("image_dir", "classpath:/static/images/");

        JasperReport report = JasperCompileManager.compileReport(filePath);
        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
        File tempFile = File.createTempFile("ReportGenerated", "pdf");
        JasperExportManager.exportReportToPdfFile(print, tempFile.getAbsolutePath());
        return tempFile;
    }

}
