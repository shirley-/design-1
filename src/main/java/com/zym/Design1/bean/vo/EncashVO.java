package com.zym.Design1.bean.vo;

import com.zym.Design1.entity.Encash;
import com.zym.Design1.entity.User;

import java.io.Serializable;

/**
 * Created on 2018/5/3.
 */
public class EncashVO extends Encash implements Serializable{
    private static final long serialVersionUID = 1L;
    private String bankAccount;
    private String depositBank;
    private String accountHolder;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }
}
