package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileStorage {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(FileStorage.class);
    private final Path pathToFileStorage;

    public FileStorage(String path) throws Exception {
        Path pathToStorage = Paths.get(path);
        if (!pathToStorage.toFile().exists()) {
            if (!pathToStorage.toFile().mkdirs()) {
                throw new Exception("Cant create " + pathToStorage);
            }
        }
        pathToFileStorage = Paths.get(path);
    }

    public void save(Meal meal) {
        log.debug("Saving meal with id " + meal.getId() + " to " + pathToFileStorage.toAbsolutePath());
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(generateFilePath(meal)))) {
            os.writeObject(meal);
        } catch (IOException e) {
            log.error("Can't save meal id " + meal.getId(), e);
        }
    }

    public Meal load(final int id) throws Exception {
        log.debug("Load meal with id " + id + "from file");
        Path path = Files.walk(pathToFileStorage).filter(f -> f.toFile().getName().equals(String.valueOf(id))).findFirst().orElse(null);
        if (path == null) {
            log.error("Can't load meal id " + id);
            throw new Exception("Meal with id " + id + " does not exist");

        }
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path.toString()))) {
            return (Meal) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't load meal id " + id, e);
        }
        throw new Exception("Meal with id " + id + " does not exist");
    }

    public Meal load(final Meal meal) throws Exception {
        log.debug("Load meal with id " + meal.getId() + "from file");
        Path path = Paths.get(generateFilePath(meal));
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path.toString()))) {
            return (Meal) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't load meal id " + meal.getId(), e);
        }
        throw new Exception("Meal with id " + meal.getId() + " does not exist");

    }

    private String generateFilePath(Meal meal) {
        log.info("generated path to id " + meal.getId());
        String path = pathToFileStorage + "/" + meal.getDate() + "/" + meal.getId();
        if (!Paths.get(path).getParent().toFile().exists()) {
            Paths.get(path).getParent().toFile().mkdirs();
        }
        return path;
    }

    private boolean exist(Meal meal) {
        Path pathToFile = Paths.get(generateFilePath(meal));
        return pathToFile.toFile().exists();
    }

    public void update(Meal meal) {
        log.debug("Updating meal with id " + meal.getId());
        Path pathToFile = Paths.get(generateFilePath(meal));
        if (!exist(meal)) {
            log.error("meal does not exist");
            throw new IllegalArgumentException("Meal with ID=" + meal.getId() + "does not exist");
        }
        pathToFile.toFile().delete();
        save(meal);
    }

    public void delete(Meal meal) {
        log.debug("Deleting meal with id " + meal.getId());
        if (!exist(meal)) {
            log.error("meal does not exist");
            throw new IllegalArgumentException("Meal with ID=" + meal.getId() + "does not exist");
        }
        Path path = Paths.get(generateFilePath(meal));
        path.toFile().delete();
    }

    public List<Meal> getAllMeals() {
        log.debug("Get all meals from " + pathToFileStorage.toAbsolutePath());
        List<Meal> meals = new ArrayList<>();
        try {
            Files.walk(pathToFileStorage).forEach(f -> {
                if (f.toFile().isFile()) {
                    try {
                        meals.add(load(Integer.parseInt(f.toFile().getName())));
                    } catch (Exception e) {
                        log.error("Something goes wrong", e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Something with dir", e);
        }
        return meals;
    }

    public static void main(String[] args) throws Exception {
        FileStorage fileStorage = new FileStorage("C://123/file_storage");
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(fileStorage::save);
        List<Meal> mealsAll = fileStorage.getAllMeals();

        mealsAll.forEach(System.out::println);
        mealsAll.get(0).setDescription("xyz");
        fileStorage.update(mealsAll.get(0));
        mealsAll = fileStorage.getAllMeals();
        mealsAll.forEach(System.out::println);
    }
}
