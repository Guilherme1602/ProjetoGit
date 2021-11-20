package org.serratec.ecomerce.controller;

import java.net.URI;

import javax.validation.Valid;

import org.serratec.ecomerce.dto.ClienteDTO;
import org.serratec.ecomerce.exception.CEPInvalidoException;
import org.serratec.ecomerce.servico.ClienteService;
import org.serratec.ecomerce.dominio.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<Object> criar(@Valid @RequestBody ClienteDTO clienteDto) {
		try {
			ClienteDTO cliente = clienteService.criar(clienteDto);
			
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}").buildAndExpand(cliente.getId())
					.toUri();
			
			return ResponseEntity.created(uri).body(cliente);	
		} catch(CEPInvalidoException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}					
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizar(@PathVariable long id, @RequestBody ClienteDTO cliente) {
		
		try {
			if(clienteService.verificar(id)) {
				cliente.setId(id);
				clienteService.criar(cliente);
				return ResponseEntity.ok(cliente);
			}
			return ResponseEntity.noContent().build() ;
		}
		catch(CEPInvalidoException ex) {
			return ResponseEntity.badRequest().body("Tivemos problemas com o CEP");
		}
	}
	
	@DeleteMapping("/deletar/{id}")
    @ResponseStatus(code = HttpStatus.GONE)
    public ResponseEntity<?> deletar(@PathVariable long id) {
		if(clienteService.verificar(id)){
			
			clienteService.deleteById(id);
			return ResponseEntity.ok("Sucesso ao deletar");
		}
		else {
			return ResponseEntity.badRequest().body("Falha ao deletar");
		}
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletarTudo(){
    	clienteService.deleteAll();
		return ResponseEntity.ok("Todos os produtos foram deletados");
    }
	
}
