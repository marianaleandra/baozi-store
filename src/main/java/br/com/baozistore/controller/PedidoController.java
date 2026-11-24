package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.model.Pedido;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RespostaPedido> criar(@Valid @RequestBody DadosPedido dados) {
        Cliente cliente = clientes.findById(dados.clienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
        Produto produto = produtos.findById(dados.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
        Pedido pedido = pedidos.save(new Pedido(cliente, produto, dados.quantidade()));
        return ResponseEntity.created(URI.create("/pedidos/" + pedido.getId())).body(RespostaPedido.de(pedido));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<RespostaPedido> listar() {
        return pedidos.findAll(Sort.by("id")).stream().map(RespostaPedido::de).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public RespostaPedido consultar(@PathVariable Long id) {
        return RespostaPedido.de(buscar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidos.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Pedido buscar(Long id) {
        return pedidos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }

    public record DadosPedido(@NotNull @Positive Long clienteId,
                              @NotNull @Positive Long produtoId,
                              @NotNull @Positive Integer quantidade) {
    }

    public record RespostaPedido(Long id, Long clienteId, Long produtoId, Integer quantidade) {

        static RespostaPedido de(Pedido pedido) {
            return new RespostaPedido(pedido.getId(), pedido.getCliente().getId(),
                    pedido.getProduto().getId(), pedido.getQuantidade());
        }
    }
}
