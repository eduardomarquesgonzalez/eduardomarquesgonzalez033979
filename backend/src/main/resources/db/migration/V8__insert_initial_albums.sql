-- V8__insert_initial_albums.sql

-- Albuns de Serj Tankian
INSERT INTO albums (title) VALUES ('Harakiri'), ('Black Blooms'), ('The Rough Dog');

INSERT INTO album_artist (album_id, artist_id)
SELECT a.id, ar.id
FROM albums a
CROSS JOIN artists ar
WHERE ar.name = 'Serj Tankian'
AND a.title IN ('Harakiri', 'Black Blooms', 'The Rough Dog');

-- Albuns de Mike Shinoda
INSERT INTO albums (title) VALUES ('The Rising Tied'), ('Post Traumatic'), ('Post Traumatic EP'), ('Where''d You Go');

INSERT INTO album_artist (album_id, artist_id)
SELECT a.id, ar.id
FROM albums a
CROSS JOIN artists ar
WHERE ar.name = 'Mike Shinoda'
AND a.title IN ('The Rising Tied', 'Post Traumatic', 'Post Traumatic EP', 'Where''d You Go');

-- Albuns de Michel Telo
INSERT INTO albums (title) VALUES ('Bem Sertanejo'), ('Bem Sertanejo - O Show (Ao Vivo)'), ('Bem Sertanejo - (1a Temporada) - EP');

INSERT INTO album_artist (album_id, artist_id)
SELECT a.id, ar.id
FROM albums a
CROSS JOIN artists ar
WHERE ar.name = 'Michel Telo'
AND a.title IN ('Bem Sertanejo', 'Bem Sertanejo - O Show (Ao Vivo)', 'Bem Sertanejo - (1a Temporada) - EP');

-- Albuns de Guns N' Roses
INSERT INTO albums (title) VALUES ('Use Your Illusion I'), ('Use Your Illusion II'), ('Greatest Hits'), ('Appetite for Destruction');

INSERT INTO album_artist (album_id, artist_id)
SELECT a.id, ar.id
FROM albums a
CROSS JOIN artists ar
WHERE ar.name = 'Guns N'' Roses'
AND a.title IN ('Use Your Illusion I', 'Use Your Illusion II', 'Greatest Hits', 'Appetite for Destruction');

-- Album colaborativo
INSERT INTO albums (title) VALUES ('The Best of Rock');

INSERT INTO album_artist (album_id, artist_id)
SELECT a.id, ar.id
FROM albums a
CROSS JOIN artists ar
WHERE a.title = 'The Best of Rock'
AND ar.name IN ('Mike Shinoda', 'Serj Tankian');
