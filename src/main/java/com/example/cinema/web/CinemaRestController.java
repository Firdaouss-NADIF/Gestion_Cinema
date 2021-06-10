package com.example.cinema.web;

import com.example.cinema.dao.CinemaRepository;
import com.example.cinema.dao.FilmRepository;
import com.example.cinema.dao.TicketRepository;
import com.example.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin("*")
@RestController
public class CinemaRestController {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @CrossOrigin("*")
    @GetMapping(path = "/imageFilm/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional
    public byte[] image(@PathVariable(name = "id") Long id) throws Exception {
        Film f = filmRepository.findById(id).get();
        String photoName = f.getPhoto().toUpperCase();
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName + ".jpg");
        System.err.println(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }

    @CrossOrigin("*")
    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm) {
        List<Ticket> listTickets = new ArrayList<Ticket>();
        ticketForm.getTickets().forEach(idTicket -> {
            Ticket ticket = ticketRepository.findById(idTicket).get();
            ticket.setNomClient(ticketForm.getNameClient());
            ticket.setReserve(true);
            ticket.setCodePayment(ticketForm.getCodePayement());
            ticketRepository.save(ticket);
            listTickets.add(ticket);
        });
        return listTickets;
    }
}
@Data
class TicketForm implements Serializable {
    private String nameClient;
    private int codePayement;
    private List<Long> tickets = new ArrayList<>();
}

