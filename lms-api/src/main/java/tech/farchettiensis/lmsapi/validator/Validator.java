package tech.farchettiensis.lmsapi.validator;

public interface Validator<T> {
    boolean validate(T input);
}