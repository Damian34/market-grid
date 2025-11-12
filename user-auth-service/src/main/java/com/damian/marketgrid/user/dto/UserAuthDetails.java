package com.damian.marketgrid.user.dto;

import java.util.List;

public record UserAuthDetails(Long id, List<String> roles) {
}
