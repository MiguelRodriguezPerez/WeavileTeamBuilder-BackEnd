DELIMITER $$

/*Los métodos anotados con @Query solo te permiten ejecutar una única consulta
Para borrar todos los pokemón tienes que cortar sus relaciones y después borrar el 
propio pokemón. La manera más eficiente de realizarlo es con un procedure almacenado
en la base de datos*/

CREATE PROCEDURE deleteAllPokemonProcedure()
BEGIN
	DELETE FROM `pokemon_data-ability_data`;
    DELETE FROM `pokemon_data-move_data`;
    DELETE FROM `pokemon_data`;
END $$

DELIMITER ;