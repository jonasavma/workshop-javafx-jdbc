package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entites.Departamento;
import model.entites.Vendedor;

public class VendedorService {

	private VendedorDao dao = DaoFactory.createVendedorrDao();

	public List<Vendedor> findAll() {
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

	public void saveOrUpadade(Vendedor obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Vendedor obj) {
		dao.deleteById(obj.getId());
	}

}
