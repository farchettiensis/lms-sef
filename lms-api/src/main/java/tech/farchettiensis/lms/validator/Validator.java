package tech.farchettiensis.lms.validator;

public interface Validator<T> {
    boolean validate(T input);
}