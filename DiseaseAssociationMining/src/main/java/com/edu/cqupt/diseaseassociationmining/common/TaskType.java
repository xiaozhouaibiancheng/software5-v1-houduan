package com.edu.cqupt.diseaseassociationmining.common;

public enum TaskType {
    disease_mining,factor_mining,disease_factor_mining;

    @Override
    public String toString() {
        return this.name();
    }
}
