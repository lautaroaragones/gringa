package com.example.afip.gringa.dto;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

public class User {

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String cuit;

    @ExcelCell(1)
    private String invoice;

    @ExcelCell(2)
    private String description;

    @ExcelCell(3)
    private String amount;

    @ExcelCell(5)
    private String email;

    private String filePath;

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
