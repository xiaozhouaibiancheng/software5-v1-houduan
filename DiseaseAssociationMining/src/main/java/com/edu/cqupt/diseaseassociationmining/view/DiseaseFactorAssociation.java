package com.edu.cqupt.diseaseassociationmining.view;

import com.edu.cqupt.diseaseassociationmining.entity.Association;
import com.edu.cqupt.diseaseassociationmining.entity.Disease;
import com.edu.cqupt.diseaseassociationmining.entity.Factor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiseaseFactorAssociation {
    private List<Disease> diseases;
    private List<Factor> factors;
    private List<Association> associations;
}
