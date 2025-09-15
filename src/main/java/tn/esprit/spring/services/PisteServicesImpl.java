package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.List;
@AllArgsConstructor
@Service
public class PisteServicesImpl implements  IPisteServices{

    private IPisteRepository pisteRepository;

    @Override
    public List<Piste> retrieveAllPistes() {
        return pisteRepository.findAll();
    }

    @Override
    public Piste addPiste(Piste piste) {
        if (piste == null) {
            throw new NullPointerException("Piste cannot be null");
        }
        return pisteRepository.save(piste);
    }

    @Override
    public void removePiste(Long numPiste) {
        if (numPiste == null) {
            throw new NullPointerException("Piste ID cannot be null");
        }
        pisteRepository.deleteById(numPiste);
    }

    @Override
    public Piste retrievePiste(Long numPiste) {
        if (numPiste == null) {
            throw new NullPointerException("Piste ID cannot be null");
        }
        return pisteRepository.findById(numPiste).orElse(null);
    }
}
