package com.devsuperior.dsclient.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entities.Client;
import com.devsuperior.dsclient.repositories.ClientRepository;
import com.devsuperior.dsclient.services.exceptions.DatabaseException;
import com.devsuperior.dsclient.services.exceptions.ResouceNotFoundException;

@Service
public class ClientService {

	@Autowired
	public ClientRepository repository;

	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> list = repository.findAll();
		return list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);// findById vai no banco de dados a tras os dados
		Client entity = obj.orElseThrow(()-> new ResouceNotFoundException("Entity not found "));
	return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	    entity = repository.save(entity);
	    
	    return new ClientDTO(entity);
		
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
		Client entity = repository.getOne(id);// O getOne nao vai no banco de dados, ele instancia um obj provisório só quando for salvar ele vai no banco 
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e ) {
			throw new ResouceNotFoundException("Id not found " + id);
			
		}
	}

	public void delete(Long id) {
		
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e ) {
			throw new ResouceNotFoundException("Id not found " + id);
			
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
	}
	
	
	
	
	
	
	
	
	

}
