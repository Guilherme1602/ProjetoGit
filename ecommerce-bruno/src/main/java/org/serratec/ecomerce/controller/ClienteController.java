package org.serratec.ecomerce.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.serratec.ecomerce.dominio.Cliente;
import org.serratec.ecomerce.dto.ClienteDTO;
import org.serratec.ecomerce.exception.CEPInvalidoException;
import org.serratec.ecomerce.repositorio.ClienteRepository;
import org.serratec.ecomerce.servico.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
	
	@Autowired 
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/todos")
	public ResponseEntity<Object> listar(){
		clienteService.listar();
		return ResponseEntity.ok().build();
	}
	
	
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
	public ResponseEntity<Object> atualizar(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails user){
		
		Cliente cliente = clienteRepository.findByNome(user.getUsername());
		
		if(clienteRepository.existsById(id)) {
		Optional<Cliente> testeCliente = clienteRepository.findById(id);
				
			if(cliente.getId() == testeCliente.get().getId()) {  // OPÇÃO 1
		        try {
		                clienteDTO.setId(id);
		                clienteService.criar(clienteDTO);
		                return ResponseEntity.ok(clienteDTO);
		        }
		        catch(CEPInvalidoException ex) {
		            return ResponseEntity.badRequest().body("CEP inválido");
		        }
			}
			return ResponseEntity.badRequest().body("O Cliente logado é inválido para esta operação");
		}
		return ResponseEntity.badRequest().body("Cliente não encontrado");
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> Deletar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
		
		Cliente cliente = clienteRepository.findByNome(user.getUsername());
		
		if(clienteRepository.existsById(id)) { 
		Optional<Cliente> testeCliente = clienteRepository.findById(id);
		
	        if(cliente.equals(testeCliente.get())){  // OPÇÃO 2

	        	clienteRepository.deleteById(id);
	            
	            return ResponseEntity.ok("Sucesso ao deletar");
	        }
	        else {
	            return ResponseEntity.badRequest().body("");
	        }
		}
		return ResponseEntity.badRequest().body("Cliente não encontrado");   
	}
}


//Cliente cliente = clienteRepository.findByNome(user.getUsername());
//
//if(cliente == null) {
//	
//	return ResponseEntity.badRequest().body("Cliente não encontrado");
//}
//
//clienteRepository.deleteById(cliente.getId());
//
//return ResponseEntity.ok("Sucesso ao deletar");

