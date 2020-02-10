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

public class FileStorage implements Storage{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(FileStorage.class);
    private final Path pathToFileStorage;

    public FileStorage(String path) {
        Path pathToStorage = Paths.get(path);
        if (!pathToStorage.toFile().exists()) {
            if (!pathToStorage.toFile().mkdirs()) {
                log.error("Cant create " + pathToStorage);
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

    public Meal load(final int id) {
        log.debug("Load meal with id " + id + "from file");
        Path path = null;
        try {
            path = getPath(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path.toString()))) {
            return (Meal) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't load meal id " + id, e);
        }
        log.error("Meal with id " + id + " does not exist");
        return null;
    }

    public Meal load(final Meal meal) throws Exception {
        log.debug("Load meal with id " + meal.getId() + "from file");
        Path path = getPath(meal.getId());
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


    public void delete(Meal meal) {
        log.debug("Deleting meal with id " + meal.getId());
        if (!exist(meal)) {
            log.error("meal does not exist");
            throw new IllegalArgumentException("Meal with ID=" + meal.getId() + "does not exist");
        }
        Path path = Paths.get(generateFilePath(meal));
        path.toFile().delete();
    }

    public void delete(int id) {
        log.debug("Deleting meal with id " + id);
        Path path = null;
        try {
            path = getPath(id);
        } catch (Exception e) {
            log.error("deleting error", e);
        }
        if (!path.toFile().exists()) {
            log.error("ID " + id + " does not exist");
        }
        path.toFile().delete();
    }

    @Override
    public void update(Meal meal) {
        delete(meal.getId());
        save(meal);
    }

    private Path getPath(int id) {
        Path path = null;
        try {
            path = Files.walk(pathToFileStorage).filter(f -> f.toFile().getName().equals(String.valueOf(id))).findFirst().orElse(null);
        } catch (IOException e) {
            log.error("file error", e);
        }
        if (path == null) {
            log.error("Can't load meal id " + id);
        }
        return path;
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
}
