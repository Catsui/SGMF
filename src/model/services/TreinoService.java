package model.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.entities.Treino;

public class TreinoService {
	
	public List<Treino> findAll() {
		List<Treino> list = new ArrayList<>();
		list.add(new Treino(1, "Defini��o", Date.from(Instant.now())));
		list.add(new Treino(2, "Emagrecimento", Date.from(Instant.now())));
		list.add(new Treino(3, "Hipertrofia", Date.from(Instant.now())));
		
		return list;
	}

}
