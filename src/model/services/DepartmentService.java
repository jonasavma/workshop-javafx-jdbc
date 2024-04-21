package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entites.Departamento;

public class DepartmentService {

	public List<Departamento> findAll() {
		// MOCK

		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Computador"));
		list.add(new Departamento(3, "Eletronicos"));

		return list;

	}

}
