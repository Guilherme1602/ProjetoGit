package org.serratec.ecomerce.repositorio;

import org.serratec.ecomerce.dominio.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {

}
