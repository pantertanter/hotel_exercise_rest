package dk.lyngby.dto;

import java.util.Set;

public record RegisterDto(String userName, String userPassword, Set<String> roleList) {}
