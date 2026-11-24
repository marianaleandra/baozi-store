package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clientes;

    public ClienteController(ClienteRepository clientes) {
        this.clientes = clientes;
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody DadosCliente dados) {
        Cliente cliente = clientes.save(new Cliente(dados.nome().trim(), dados.clienteDesde()));
        return ResponseEntity.created(URI.create("/clientes/" + cliente.getId())).body(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return clientes.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public Cliente consultar(@PathVariable Long id) {
        return clientes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clientes.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }

    public record DadosCliente(@NotBlank @Size(max = 120) String nome,
                               @NotNull LocalDate clienteDesde) {
    }
}
