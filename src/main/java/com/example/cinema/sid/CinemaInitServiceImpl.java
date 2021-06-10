package com.example.cinema.sid;

import com.example.cinema.dao.*;
import com.example.cinema.entities.*;
import com.example.cinema.sid.ICinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    int codepayement=1;


    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
            Ville ville = new Ville();
            ville.setName(nameVille);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v->{
            Stream.of("MegaRama","Imax","Founoun","Chahrazad","Daouliz").forEach(name ->{
                Cinema cinema = new Cinema();
                cinema.setName(name);
                cinema.setNombreSalles(3+(int)(Math.random()*7));
                cinema.setAltitude(3+(int)(Math.random()*10)+Math.random()*10);
                cinema.setLatitude(3+(int)(Math.random()*10)+Math.random()*10);
                cinema.setLongitude(3+(int)(Math.random()*10)+Math.random()*10);
                cinema.setVille(v);
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema->{
            for (int i = 0; i < cinema.getNombreSalles(); i++) {
                Salle salle = new Salle();
                salle.setName("Salle "+(i+1));
                salle.setCinema(cinema);
                salle.setNombrePlace((int) (15+((Math.random()*20))));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle->{
            for (int i = 0; i < salle.getNombrePlace(); i++) {
                Place place = new Place();
                place.setNumero((i+1));
                place.setAltitude(1 + (int) (Math.random() * 10) + Math.random() * 10);
                place.setLatidude(1 + (int) (Math.random() * 10) + Math.random() * 10);
                place.setLongtitude(1 + (int) (Math.random() * 10) + Math.random() * 10);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initSeances() {
        Stream.of("12:00","15:00","17:00","19:00","21:00").forEach( s ->{
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(timeFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("History","Action","Drama","Suspence").forEach(cat->{
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categorieRepository.save(categorie);
        });
    }


    @Override
    public void initFilms() {
        double[] durees = new double[]{1,1.5,2,2.5,3};
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("SevenPounds","Focus","TheShawnsankRedemption","mircaleInCellN7").forEach(movie -> {
            Film film = new Film();
            film.setTitre(movie);
            try {
                film.setDateSortie(dateFormat.parse("18/01/2021"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setDescription("Good movies");
            film.setPhoto(movie);
            film.setRealisateur("Random");
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prices = new double[]{30,50,70,90,100};
        cinemaRepository.findAll().forEach(cinema -> {
            cinema.getSalles().forEach( salle -> {
                List<Film> films = filmRepository.findAll();
                seanceRepository.findAll().forEach(seance -> {
                    Projection projection=new Projection();
                    projection.setFilm(filmRepository.getOne(1+((long) (Math.random() * (filmRepository.findAll().size())))));
                    projection.setPrix(prices[new Random().nextInt(prices.length)]);
                    projection.setSeance(seance);
                    projection.setSalle(salle);
                    try {
                        projection.setDateProjection(dateFormat.parse("01/01/2021"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    projectionRepository.save(projection);
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setCodePayment(codepayement++);
                ticket.setPlace(place);
                ticket.setPrix(projection.getPrix());
                ticket.setProjection(projection);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }
}
