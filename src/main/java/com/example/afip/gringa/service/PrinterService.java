package com.example.afip.gringa.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;


@Service
public class PrinterService {

    @Value("${printer.name}")
    private String printerName;

    public void print(String path) {

        this.listarImpresoras();

        try {
            this.sendPrint(path);
        } catch (IOException | PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error de impresion", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendPrint(String path) throws IOException, PrinterException {

        PDDocument document = null;
        PDDocument document1 = null;

        try {
            File pdfFile = new File(path);

            PrintService myPrintService = this.findPrintService(printerName);
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            document = PDDocument.load(pdfFile);

            PDPage doc = document.getPage(0);

            document1 = new PDDocument();

            document1.addPage(doc);

            printerJob.setPageable(new PDFPageable(document1));
            printerJob.setPrintService(myPrintService);

            printerJob.print();
        } finally {
            if (document != null) {
                document1.close();
                document.close();
            }

        }

    }

    private PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());

            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

    public void listarImpresoras() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Lista de impresoras disponibles");

        for (PrintService printService : printServices) {
            System.out.println("\t" + printService);
        }
    }
}
