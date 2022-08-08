package com.audit.response;

import java.util.ArrayList;
import java.util.List;

import com.audit.dto.EvaluationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class EvaluatedShowResponse {
	private List<EvaluationDTO> listEva = new ArrayList<>();
	private Double gpa;	
}
