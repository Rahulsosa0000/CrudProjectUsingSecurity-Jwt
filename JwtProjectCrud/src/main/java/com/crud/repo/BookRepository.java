package com.crud.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.entity.Books;


public interface BookRepository extends JpaRepository<Books, Integer> {


}
