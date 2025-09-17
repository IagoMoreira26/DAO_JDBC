package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

		System.out.println("=== TEST 1: Department Find by ID ===");
		Department department = departmentDao.findById(3);
		System.out.println(department);

		System.out.println("\n=== TEST 2: Department Find All ===");
		List<Department> list = departmentDao.findAll();
		for (Department dep : list) {
			System.out.println(dep);
		}

		System.out.println("\n=== TEST 3: Department Insert ===");
		Department newDepartment = new Department(null, "Músicas");
		departmentDao.insert(newDepartment);
		System.out.println("Inserted! New id = " + newDepartment.getId());

		System.out.println("\n=== TEST 4: Department Update ===");
		department = departmentDao.findById(1);

		department.setName("Espadas");
		departmentDao.update(department);
		System.out.println("Update Completed!");

		System.out.println("\n=== TEST 5: Seller Delete ===");
		System.out.print("Digite um ID para teste de deleção: ");
		int id = sc.nextInt();

		departmentDao.deleteById(id);
		System.out.println("Delete Completed!");

		sc.close();
	}
}
