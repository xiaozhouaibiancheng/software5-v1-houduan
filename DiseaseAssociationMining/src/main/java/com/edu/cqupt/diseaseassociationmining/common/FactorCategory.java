package com.edu.cqupt.diseaseassociationmining.common;

public enum FactorCategory {
//    clinical_fratures,lifestyle_habits,social_environment,others;
    head,thoracodorsal,belly,limb,others;
    public String toString() {
        return this.name();
    }
}
