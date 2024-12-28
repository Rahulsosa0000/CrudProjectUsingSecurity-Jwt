package com.crud.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.model.Books;

public interface BookRepository extends JpaRepository<Books, Integer> {

	

}
