## Description

The user-auth-adapter api is used as library to map incoming headers into spring security UserDetails.
However for now it is only PoC version and implementation can be improved or changed into other way
e.g. simple annotation mapping based on the headers, however this could skip a lot of spring mechanics,
like roles recognition, but also be more effective and simpler because skipping most of the infrastructure.

