package model.dao;

import java.util.List;

import model.entites.Departamento;
import model.entites.Vendedor;


public interface VendedorDao {

	void insert(Vendedor obj);
	void update(Vendedor obj);
	void deleteById(Integer id);
	Vendedor findById(Integer id);
	List<Vendedor> findAll();
	List<Vendedor> findByDepartment(Departamento departmento);
}
