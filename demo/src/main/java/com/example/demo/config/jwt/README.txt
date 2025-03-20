Todas estas clases las tuve que declarar para que
spring pudiera recibir solicitudes del lado cliente incluso para operaciones
de usuarios no logueados.

No he declarado controlador para la autenticación porque no realizaré logins
a través de jwt. La idea es cambiar esto eventualmente por 0Auth2