package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entites.Departamento;

public class DepartmentService {

	private DepartamentoDao dao = DaoFactory.createDepartmentDao();

	public List<Departamento> findAll() {
		// MOCK

		/*
		 * List<Departamento> list = new ArrayList<>(); list.add(new Departamento(1,
		 * "Livros")); list.add(new Departamento(2, "Computador")); list.add(new
		 * Departamento(3, "Eletronicos"));
		 * 
		 * return list;
		 */

		return dao.findAll();

	}

	public void saveOrUpadade(Departamento obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Departamento obj) {
		dao.deleteById(obj.getId());
	}

}
