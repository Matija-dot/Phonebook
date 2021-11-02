CREATE TABLE IF NOT EXISTS `Phonebook`.`Contact` (
  `contactID` INT GENERATED ALWAYS AS () VIRTUAL,
  `phoneNumber` VARCHAR(45) NULL,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `streetAddress` VARCHAR(45) NULL,
  `town` VARCHAR(45) NULL,
  `birthDate` DATE NULL,
  PRIMARY KEY (`contactID`),
  UNIQUE INDEX `contactID_UNIQUE` (`contactID` ASC) VISIBLE)
ENGINE = InnoDB