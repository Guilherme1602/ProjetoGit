package org.serratec.ecomerce.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.serratec.ecomerce.dominio.Cliente;
import org.serratec.ecomerce.dominio.Pedido;
import org.serratec.ecomerce.dominio.Produto;
import org.serratec.ecomerce.dto.PedidoDTO;
import org.serratec.ecomerce.exception.PedidoNaoExisteException;
import org.serratec.ecomerce.repositorio.ClienteRepository;
import org.serratec.ecomerce.repositorio.PedidoRepository;
import org.serratec.ecomerce.repositorio.ProdutoRepository;
import org.serratec.ecomerce.servico.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired 
	private ProdutoRepository produtoRepository;
	
	@Autowired 
	private PedidoRepository pedidoRepository;
	
	@Autowired 
	private ClienteRepository clienteRepository;
	
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody PedidoDTO pedidoDTO, @AuthenticationPrincipal UserDetails user) {

		PedidoDTO pedido = pedidoService.criar(pedidoDTO, user);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}").buildAndExpand(pedido.getId())
				.toUri();

		return ResponseEntity.created(uri).body(pedido);
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizar(@Valid @RequestBody PedidoDTO pedidoDTO, @PathVariable long id, @AuthenticationPrincipal UserDetails user) throws PedidoNaoExisteException {
		
		Cliente cliente = clienteRepository.findByNome(user.getUsername());
			
		if(pedidoRepository.existsById(id)) {
		Optional<Pedido> testeclientePedido = pedidoRepository.findById(id);
			
			if(cliente.getId() == testeclientePedido.get().getCliente().getId()) {
			
				if(testeclientePedido.get().isFinalizado() == false){
					try {
						List<Produto> listatest = produtoRepository.findAllById(pedidoDTO.getProdutos());
						if(listatest.size() != pedidoDTO.getProdutos().size()) {
							return ResponseEntity.badRequest().body("Produto(s) não encontrado(s).");
						}
						PedidoDTO pedidoatualizado = pedidoService.atualizar(pedidoDTO, id);
						
						URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedidoatualizado.getId()).toUri();
						return ResponseEntity.created(uri).body(pedidoatualizado);
					}catch (PedidoNaoExisteException pnee) {
						return ResponseEntity.badRequest().body(pnee.getMessage());
					}
				} 
				return ResponseEntity.badRequest().body("O pedido selecionado já foi finalizado");
			}
			return ResponseEntity.badRequest().body("O Cliente logado não é o mesmo do pedido");
		}
		return ResponseEntity.badRequest().body("O pedido não existe");
	}

	@PostMapping("/finalizar/{id}")
	public ResponseEntity<?> finalizarPedido(long id, @AuthenticationPrincipal UserDetails user) {
		
		Cliente cliente = clienteRepository.findByNome(user.getUsername());
		
		if(pedidoRepository.existsById(id)) {
			Optional<Pedido> testeclientePedido = pedidoRepository.findById(id);
			
			if(cliente.getId() == testeclientePedido.get().getCliente().getId()) {
		
				try {
					pedidoService.finalizar(id);
					return ResponseEntity.ok().build();
				} catch (PedidoNaoExisteException pnee) {
					return ResponseEntity.badRequest().body(pnee.getMessage());
				}
			}
			return ResponseEntity.badRequest().body("O Cliente logado não é o mesmo do pedido");
		}
		return ResponseEntity.badRequest().body("O pedido não existe");
	}	
	
}
