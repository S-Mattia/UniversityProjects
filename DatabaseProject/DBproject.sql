CREATE	TABLE	UserBase	(	
Identificativo	VARCHAR(255)	PRIMARY	KEY,	
Tipo	VARCHAR(50)	NOT	NULL	
);	
CREATE	TABLE	Utente	(	
Email	VARCHAR(255)	PRIMARY	KEY,	
Nome	VARCHAR(100)	NOT	NULL,	
Password	VARCHAR(255)	NOT	NULL,	
N_Telefono	VARCHAR(15)	NOT	NULL,	
Indirizzo	VARCHAR(255)	NOT	NULL,	
Borsellino	DECIMAL(10,	2)	DEFAULT	0,	
Premium	BOOLEAN	DEFAULT	false,	
CONSTRAINT	fk_utente_user	FOREIGN	KEY	(Email)	REFERENCES	UserBase(Identificativo)	ON	
UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Ristorante	(	
Nome_Ristorante	VARCHAR(255)	PRIMARY	KEY,	
Descrizione	TEXT,	
Costo_spedizione	DECIMAL(5,	2)	DEFAULT	0,	
Immagine	TEXT,	
Voto	REAL	CHECK	(Voto	>=	0	AND	Voto	<=	5),	
Indirizzo	VARCHAR(255)	NOT	NULL,	
Top_Partner	DATE	DEFAULT	NULL,	
n_ordini_annullati	INT	DEFAULT	0,	
n_ordini_reclamo	INT	DEFAULT	0,	
CONSTRAINT	fk_ristorante_user	FOREIGN	KEY	(Nome_Ristorante)	REFERENCES	
UserBase(Identificativo)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Rider	(	
Codice_rider	VARCHAR(255)	PRIMARY	KEY,	
Stato	VARCHAR(50)	NOT	NULL,	
Mezzo	VARCHAR(50)	NOT	NULL,	
Posizione	VARCHAR(255),	
Voto	REAL	CHECK	(Voto	>=	0	AND	Voto	<=	5),	
KM_Rimanenti	DECIMAL(5,	2)	DEFAULT	NULL,	
CONSTRAINT	fk_rider_user	FOREIGN	KEY	(Codice_rider)	REFERENCES	UserBase(Identificativo)	
ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);


CREATE	TABLE	Metodo_di_Pagamento	(	
Codice_carta	VARCHAR(16)	PRIMARY	KEY,	
Nome_intestatario	VARCHAR(100)	NOT	NULL,	
Scadenza	DATE	NOT	NULL,	
Codice_Segreto	CHAR(3)	NOT	NULL	
);	
CREATE	TABLE	Utilizza	(	
Email	VARCHAR(255),	
Codice_carta	VARCHAR(16),	
CONSTRAINT	pk_Utilizza	PRIMARY	KEY	(Email,	Codice_carta),	
CONSTRAINT	fk_utilizza_Email	FOREIGN	KEY	(Email)	REFERENCES	Utente(Email)	ON	UPDATE	
CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_utilizza_carta	FOREIGN	KEY	(Codice_carta)	REFERENCES	
Metodo_di_Pagamento(Codice_carta)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Sconto	(	
Codice_sconto	VARCHAR(50)	PRIMARY	KEY,	
Valore_percentuale	SMALLINT	NOT	NULL	CHECK	(Valore_percentuale	>	0	AND	
Valore_percentuale	<=	100)	
);	
CREATE	TABLE	Sconta	(	
Email	VARCHAR(255),	
Codice_sconto	VARCHAR(50),	
CONSTRAINT	pk_Sconta	PRIMARY	KEY	(Email,	Codice_sconto),	
CONSTRAINT	fk_Sconta_Email	FOREIGN	KEY	(Email)	REFERENCES	Utente(Email)	ON	UPDATE	
CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_Sconta_Sconto	FOREIGN	KEY	(Codice_sconto)	REFERENCES	
Sconto(Codice_sconto)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Piatto	(	
Titolo	VARCHAR(255),	
Nome_Ristorante	VARCHAR(255),	
Prezzo	DECIMAL(5,	2)		NOT	NULL,	
Immagine	TEXT,	
Lista_allergeni	TEXT		NOT	NULL,	
Lista_ingredienti	TEXT		NOT	NULL,	
Sconto	SMALLINT	NOT	NULL	CHECK	(Sconto	>=	0	AND	Sconto	<=	100)	DEFAULT	0,	
CONSTRAINT	pk_Pitto	PRIMARY	KEY	(Titolo,	Nome_Ristorante),	
CONSTRAINT	fk_Ristorante_Cucina	FOREIGN	KEY	(Nome_Ristorante)	REFERENCES	
Ristorante(Nome_Ristorante)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Categoria	(	
Nome	VARCHAR(100)	PRIMARY	KEY	
);	
CREATE	TABLE	Categorizza	(	
Titolo_Piatto	VARCHAR(255),	
Nome_Ristorante	VARCHAR(255),	
Nome_Categoria	VARCHAR(100),	
CONSTRAINT	pk_Categorizza	PRIMARY	KEY	(Titolo_Piatto,	Nome_Ristorante,	Nome_Categoria),	
CONSTRAINT	fk_Categorizza_Piatto	FOREIGN	KEY	(Titolo_Piatto,	Nome_Ristorante)	REFERENCES	
Piatto(Titolo,	Nome_Ristorante)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_Categorizza_Categoria	FOREIGN	KEY	(Nome_Categoria)	REFERENCES	
Categoria(Nome)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Ordine	(	
Email_Utente	VARCHAR(255),	
Time_stamp	TIMESTAMP,	
Codice_Rider	VARCHAR(255),	
Stato	VARCHAR(50)	DEFAULT	'In	preparazione',	
Mancia	DECIMAL(3,	1)	DEFAULT	0,	
Time_stamp_Consegna	TIMESTAMP,	
CONSTRAINT	pk_Ordine	PRIMARY	KEY	(Email_Utente,	Time_stamp),	
CONSTRAINT	fk_ordine_utente	FOREIGN	KEY	(Email_Utente)	REFERENCES	Utente(Email)	ON	
UPDATE	CASCADE,	
CONSTRAINT	fk_ordine_rider	FOREIGN	KEY	(Codice_Rider)	REFERENCES	Rider(Codice_rider)	ON	
UPDATE	CASCADE	ON	DELETE	SET	NULL	
);	
CREATE	TABLE	Specificato	(	
Titolo_Piatto	VARCHAR(255),	
Nome_Ristorante	VARCHAR(255),	
Email_Utente	VARCHAR(255),	
Time_stamp	TIMESTAMP,	
Quantità	SMALLINT	CHECK	(Quantità	>	0)	DEFAULT	1,	
CONSTRAINT	pk_Specificato	PRIMARY	KEY	(Titolo_Piatto,	Nome_Ristorante,	Email_Utente,	
Time_stamp),	
CONSTRAINT	fk_Specificato_Piatto	FOREIGN	KEY	(Titolo_Piatto,	Nome_Ristorante)	REFERENCES	
Piatto(Titolo,	Nome_Ristorante)	ON	UPDATE	CASCADE,	
CONSTRAINT	fk_Specificato_Ordine	FOREIGN	KEY	(Email_Utente,	Time_stamp)	REFERENCES	
Ordine(Email_Utente,	Time_stamp)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Chat	(	
Destinatario	VARCHAR(255),	
Mittente	VARCHAR(255),	
Data_e_ora	TIMESTAMP,	
Messaggio	TEXT	NOT	NULL,	
Timestamp_ordine	TIMESTAMP,	
Email_Utente	varchar(255),	
Tipo	varchar(50),	
CONSTRAINT	pk_Chat	PRIMARY	KEY	(Destinatario,	Mittente,	Data_e_ora),	
CONSTRAINT	fk_Chat_Destinatario	FOREIGN	KEY	(Destinatario)	REFERENCES	
UserBase(Identificativo)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_Chat_Mittente	FOREIGN	KEY	(Mittente)	REFERENCES	UserBase(Identificativo)	
ON	UPDATE	CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_Chat_Riferimento	FOREIGN	KEY	(Timestamp_ordine,Email_Utente)	
REFERENCES	Ordine(Time_stamp,	Email_Utente)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);	
CREATE	TABLE	Valutazione	(	
Email_Utente	VARCHAR(255),	
Time_stamp_Ordine	TIMESTAMP,	
Identificativo_valutato	VARCHAR(255),	
Commento	TEXT,	
Voto	SMALLINT	CHECK	(Voto	>=	0	AND	Voto	<=	5),	
CONSTRAINT	pk_Valutazione	PRIMARY	KEY	(Email_Utente,	Time_stamp_Ordine,	
Identificativo_valutato),	
CONSTRAINT	fk_valutazione_ordine	FOREIGN	KEY	(Email_Utente,	Time_stamp_Ordine)	
REFERENCES	Ordine(Email_Utente,	Time_stamp)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE,	
CONSTRAINT	fk_valutazione_user	FOREIGN	KEY	(Identificativo_valutato)	REFERENCES	
UserBase(Identificativo)	ON	UPDATE	CASCADE	ON	DELETE	CASCADE	
);
