package cfrdocarmo.veiculo.domain.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfrdocarmo.veiculo.domain.model.MarcaFabricante;
import cfrdocarmo.veiculo.domain.model.Veiculo;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
	List<Veiculo> findByMarcaAndAno(MarcaFabricante marca, Integer ano);
	
	List<Veiculo> findByMarcaOrAno(MarcaFabricante marca, Integer ano);
}
