/**
 * Online Examination System
 * 
 * Features:
 * - Student with name
 * - Examination containing multiple Questions
 * - Abstract Question class with MultipleChoiceQuestion, TrueFalseQuestion, and ShortAnswerQuestion subclasses
 * - Individual evaluation logic and point assignment per question type
 * - Attempt tracking student, exam, answers, and state (IN_PROGRESS, SUBMITTED)
 * - Answer recording student responses
 * - Examination workflow: start exam -> answer questions -> submit -> score calculation
 * - Immutability enforcement post-submission
 */

import java.util.*;

public class Q3_OnlineExaminationSystem {

    // ==========================================
    // Student Class
    // ==========================================
    public static class Student {
        private final String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Starts an examination for this student and creates an Attempt.
         */
        public Attempt startExam(Examination exam) {
            Attempt attempt = new Attempt(this, exam);
            System.out.println(exam.getTitle() + " started by " + this.name + ".");
            return attempt;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // ==========================================
    // Abstract Question Class & Subclasses
    // ==========================================
    public static abstract class Question {
        private final String id;
        private final String text;
        private final int points;

        public Question(String id, String text, int points) {
            this.id = id;
            this.text = text;
            this.points = points;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public int getPoints() {
            return points;
        }

        /**
         * Evaluates whether the given answer is correct according to the question's rules.
         */
        public abstract boolean evaluate(String studentAnswer);
    }

    /**
     * Multiple Choice Question with options and a correct option choice (e.g., "A", "B", etc.)
     */
    public static class MultipleChoiceQuestion extends Question {
        private final List<String> options;
        private final String correctOption;

        public MultipleChoiceQuestion(String id, String text, int points, List<String> options, String correctOption) {
            super(id, text, points);
            this.options = new ArrayList<>(options);
            this.correctOption = correctOption;
        }

        public List<String> getOptions() {
            return Collections.unmodifiableList(options);
        }

        public String getCorrectOption() {
            return correctOption;
        }

        @Override
        public boolean evaluate(String studentAnswer) {
            if (studentAnswer == null) {
                return false;
            }
            return studentAnswer.trim().equalsIgnoreCase(correctOption.trim());
        }
    }

    /**
     * True/False Question
     */
    public static class TrueFalseQuestion extends Question {
        private final boolean correctAnswer;

        public TrueFalseQuestion(String id, String text, int points, boolean correctAnswer) {
            super(id, text, points);
            this.correctAnswer = correctAnswer;
        }

        public boolean isCorrectAnswer() {
            return correctAnswer;
        }

        @Override
        public boolean evaluate(String studentAnswer) {
            if (studentAnswer == null) {
                return false;
            }
            String trimmed = studentAnswer.trim().toLowerCase();
            if (trimmed.equals("true") || trimmed.equals("t")) {
                return correctAnswer;
            } else if (trimmed.equals("false") || trimmed.equals("f")) {
                return !correctAnswer;
            }
            return false;
        }
    }

    /**
     * Short Answer Question evaluated against expected answer (case-insensitive)
     */
    public static class ShortAnswerQuestion extends Question {
        private final String expectedAnswer;

        public ShortAnswerQuestion(String id, String text, int points, String expectedAnswer) {
            super(id, text, points);
            this.expectedAnswer = expectedAnswer;
        }

        public String getExpectedAnswer() {
            return expectedAnswer;
        }

        @Override
        public boolean evaluate(String studentAnswer) {
            if (studentAnswer == null) {
                return false;
            }
            return studentAnswer.trim().equalsIgnoreCase(expectedAnswer.trim());
        }
    }

    // ==========================================
    // Examination Class
    // ==========================================
    public static class Examination {
        private final String title;
        private final List<Question> questions;

        public Examination(String title) {
            this.title = title;
            this.questions = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void addQuestion(Question question) {
            questions.add(question);
        }

        public List<Question> getQuestions() {
            return Collections.unmodifiableList(questions);
        }

        public int getTotalPoints() {
            int total = 0;
            for (Question q : questions) {
                total += q.getPoints();
            }
            return total;
        }
    }

    // ==========================================
    // Answer Class
    // ==========================================
    public static class Answer {
        private final Question question;
        private final String studentResponse;

        public Answer(Question question, String studentResponse) {
            this.question = question;
            this.studentResponse = studentResponse;
        }

        public Question getQuestion() {
            return question;
        }

        public String getStudentResponse() {
            return studentResponse;
        }
    }

    // ==========================================
    // State Enum & Attempt Class
    // ==========================================
    public enum AttemptState {
        IN_PROGRESS,
        SUBMITTED
    }

    public static class Attempt {
        private final Student student;
        private final Examination examination;
        private final Map<Question, Answer> answers;
        private AttemptState state;

        public Attempt(Student student, Examination examination) {
            this.student = student;
            this.examination = examination;
            this.answers = new LinkedHashMap<>();
            this.state = AttemptState.IN_PROGRESS;
        }

        public Student getStudent() {
            return student;
        }

        public Examination getExamination() {
            return examination;
        }

        public AttemptState getState() {
            return state;
        }

        /**
         * Records or updates an answer for a specific question.
         * Rejects modification if the attempt is already submitted.
         */
        public void answerQuestion(Question question, String response) {
            if (state == AttemptState.SUBMITTED) {
                System.out.println("Cannot change answers for a submitted examination.");
                return;
            }
            answers.put(question, new Answer(question, response));
            System.out.println("Answer recorded for " + question.getId() + ".");
        }

        /**
         * Submits the examination attempt.
         */
        public void submit() {
            if (state == AttemptState.SUBMITTED) {
                System.out.println("Examination has already been submitted.");
                return;
            }
            this.state = AttemptState.SUBMITTED;
            System.out.println(examination.getTitle() + " submitted by " + student.getName() + ".");
        }

        /**
         * Calculates the score and displays detailed results.
         */
        public void displayResult() {
            int totalScore = 0;
            int totalPossible = examination.getTotalPoints();
            List<String> questionResults = new ArrayList<>();

            for (Question question : examination.getQuestions()) {
                Answer answer = answers.get(question);
                boolean isCorrect = answer != null && question.evaluate(answer.getStudentResponse());
                if (isCorrect) {
                    totalScore += question.getPoints();
                    questionResults.add(question.getId() + ": Correct (" + question.getPoints() + " points)");
                } else {
                    questionResults.add(question.getId() + ": Incorrect (0 points)");
                }
            }

            System.out.println("Result: " + String.join(", ", questionResults) + ". Total score: " + totalScore + "/" + totalPossible + ".");
        }
    }

    // ==========================================
    // Main Method Demonstrating Requirements
    // ==========================================
    public static void main(String[] args) {
        // 1. Create an Examination
        Examination exam = new Examination("Exam A");

        // 2. Create questions of different types
        Question q1 = new MultipleChoiceQuestion(
            "Question 1",
            "Which of the following is not a primitive type in Java?",
            5,
            Arrays.asList("A) int", "B) boolean", "C) String", "D) double"),
            "C"
        );

        Question q2 = new TrueFalseQuestion(
            "Question 2",
            "In Java, arrays are objects.",
            5,
            true
        );

        // Add questions to Exam
        exam.addQuestion(q1);
        exam.addQuestion(q2);

        // 3. Create a Student
        Student student = new Student("Student 1");

        // 4. Student starts the exam
        Attempt attempt = student.startExam(exam);

        // 5. Student answers questions
        // Question 1: Correct answer ("C")
        attempt.answerQuestion(q1, "C");

        // Question 2: Incorrect answer ("False")
        attempt.answerQuestion(q2, "False");

        // 6. Student submits the exam
        attempt.submit();

        // 7. Calculate and display score after submission
        attempt.displayResult();

        // 8. Attempt to change answer after submission (should be rejected)
        attempt.answerQuestion(q1, "A");
    }
}
