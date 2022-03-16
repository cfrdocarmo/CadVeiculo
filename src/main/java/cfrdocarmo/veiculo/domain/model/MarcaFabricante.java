package cfrdocarmo.veiculo.domain.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum MarcaFabricante {

	VOLKSWAGEN('1'),
	CHEVROLET('2'),
	FIAT('3'),
	RENAULT('4'),
	HONDA('5'),
	TOYOTA('6'),
	NISSAN('7');
	
	private final char marca;
	
	MarcaFabricante(char marca) {
		this.marca = marca;
	}

	public char getMarca() {
		return marca;
	}
	
	 @Converter(autoApply = true)
	    public static class Mapeador implements AttributeConverter<MarcaFabricante, String> {

	        @Override
	        public String convertToDatabaseColumn(MarcaFabricante x) {
	            return String.valueOf(x.getMarca());
	        }

	        @Override
	        public MarcaFabricante convertToEntityAttribute(String y) {
	            if (y == null) return null;
	            if ("1".equals(y)) return VOLKSWAGEN;
	            if ("2".equals(y)) return CHEVROLET;
	            if ("3".equals(y)) return FIAT;
	            if ("4".equals(y)) return RENAULT;
	            if ("5".equals(y)) return HONDA;
	            if ("6".equals(y)) return TOYOTA;
	            if ("7".equals(y)) return NISSAN;
	            
	            throw new IllegalStateException("Valor inválido: " + y);
	        }
	    }
}


