package com.damian.marketgrid.dto;

import java.util.List;

public record UserAuthDetails(Long id, List<String> roles) {
}
