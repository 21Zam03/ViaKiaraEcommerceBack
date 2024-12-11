package com.zam.security.services;

import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;

public interface ReportService {

    public File generateReport(String saleId, String date, Double totalPrice, String client,
                               String productQuantity) throws JRException, IOException;

}
