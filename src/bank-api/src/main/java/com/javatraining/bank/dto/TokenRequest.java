package com.javatraining.bank.dto;

import java.util.List;

public record TokenRequest(String userName, List<String> scopes) {}
