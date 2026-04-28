package com.lms.controller;

import com.lms.entity.Course;
import com.lms.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // ✅ CREATE COURSE
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course c) {

        if (c.getTitle() == null || c.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is required");
        }

        Course saved = courseRepository.save(c);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL COURSES
    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    // ✅ GET COURSE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ UPDATE COURSE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course c) {

        var opt = courseRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course existing = opt.get();

        if (c.getTitle() != null) existing.setTitle(c.getTitle());
        if (c.getDescription() != null) existing.setDescription(c.getDescription());
        if (c.getInstructorUsername() != null)
            existing.setInstructorUsername(c.getInstructorUsername());

        Course updated = courseRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE COURSE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {

        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        courseRepository.deleteById(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    // ✅ GET COURSES BY INSTRUCTOR
    @GetMapping("/mine")
    public ResponseEntity<List<Course>> myCourses(
            @RequestParam(required = false) String instructorUsername) {

        if (instructorUsername == null || instructorUsername.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(
                courseRepository.findByInstructorUsername(instructorUsername)
        );
    }
}