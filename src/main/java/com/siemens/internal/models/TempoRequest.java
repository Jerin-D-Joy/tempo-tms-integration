package com.siemens.internal.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class TempoRequest {

    @NonNull
    private final String from;

    @NonNull
    private final String to;

    @NonNull
    private final String[] worker;

}
