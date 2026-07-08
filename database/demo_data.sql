USE civitas_db;
-- MySQL dump 10.13  Distrib 9.6.0, for macos26.3 (arm64)
--
-- Host: localhost    Database: civitas_db
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--


--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'founder','founder@test.it','65536:GtqyfLBvfqqJb6dnZNKeMw==:KMdCl9ZyOoyzpQtbwTZaUsYBDHHXIhn1oYBZsJjRasA=','2026-07-08 13:02:53'),(2,'minister','minister@test.it','65536:qTIPdo2To6QT9e7HS39csA==:8XiK04/cTQTfHM5HRN40E2jAilcA2bHhwovVpyQCEdg=','2026-07-08 13:03:11'),(3,'citizen','citizen@test.it','65536:Xs8zLQ0q8B2DRQIw2BHO7Q==:Ci+Z4Vd+wgTXcI0rhPH/IMMuiWLz7OWo4zwEUFhQi3M=','2026-07-08 13:03:32');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `nations`
--

LOCK TABLES `nations` WRITE;
/*!40000 ALTER TABLE `nations` DISABLE KEYS */;
INSERT INTO `nations` VALUES (1,'Repubblica del Caffè',NULL,'Micro-nazione fondata sull’energia civica, sulle pause istituzionali e sulla partecipazione democratica dei cittadini.','☕️',1,'2026-07-08 13:04:05'),(2,'Regno dei Bug',NULL,'Micro-nazione tecnologica in cui i cittadini discutono leggi, comunicati e decisioni per mantenere stabile il sistema.','🐞',1,'2026-07-08 13:05:32');
/*!40000 ALTER TABLE `nations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `memberships`
--

LOCK TABLES `memberships` WRITE;
/*!40000 ALTER TABLE `memberships` DISABLE KEYS */;
INSERT INTO `memberships` VALUES (1,1,1,'FOUNDER','2026-07-08 13:04:05'),(2,1,2,'FOUNDER','2026-07-08 13:05:32'),(3,2,2,'MINISTER','2026-07-08 13:06:07'),(4,2,1,'MINISTER','2026-07-08 13:06:11'),(5,3,1,'CITIZEN','2026-07-08 13:06:35');
/*!40000 ALTER TABLE `memberships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `announcements`
--

LOCK TABLES `announcements` WRITE;
/*!40000 ALTER TABLE `announcements` DISABLE KEYS */;
INSERT INTO `announcements` VALUES (1,1,1,'Apertura ufficiale della Repubblica del Caffè','Il governo comunica l’apertura ufficiale delle attività civiche della micro-nazione. I cittadini sono invitati a partecipare alla vita politica nazionale.','2026-07-08 13:11:19'),(2,1,1,'Avvio delle consultazioni legislative','Sono aperte le consultazioni per proporre nuove leggi, discutere le priorità nazionali e rafforzare la stabilità istituzionale della Repubblica del Caffè.','2026-07-08 13:11:30');
/*!40000 ALTER TABLE `announcements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `laws`
--

LOCK TABLES `laws` WRITE;
/*!40000 ALTER TABLE `laws` DISABLE KEYS */;
INSERT INTO `laws` VALUES (1,1,2,'Legge sulla pausa istituzionale','Ogni cittadino può proporre una pausa simbolica durante le assemblee nazionali per favorire partecipazione, confronto e stabilità della micro-nazione.','REPEALED','2026-07-08 13:12:21','2026-07-08 13:13:53'),(2,1,2,'Legge sulle tazze obbligatorie','Ogni cittadino dovrebbe possedere una tazza ufficiale della micro-nazione da utilizzare durante le assemblee e le consultazioni pubbliche.','REJECTED','2026-07-08 13:14:09','2026-07-08 13:14:57');
/*!40000 ALTER TABLE `laws` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `votes`
--

LOCK TABLES `votes` WRITE;
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
INSERT INTO `votes` VALUES (1,1,2,'YES','2026-07-08 13:12:26'),(2,1,1,'YES','2026-07-08 13:13:08'),(3,1,3,'ABSTAIN','2026-07-08 13:13:27'),(4,2,2,'NO','2026-07-08 13:14:12'),(5,2,3,'NO','2026-07-08 13:14:31'),(6,2,1,'YES','2026-07-08 13:14:55');
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `decision_logs`
--

LOCK TABLES `decision_logs` WRITE;
/*!40000 ALTER TABLE `decision_logs` DISABLE KEYS */;
INSERT INTO `decision_logs` VALUES (1,1,NULL,1,'ROLE_UPDATED','Ruolo dell\'utente ID 2 modificato da cittadino a ministro.','2026-07-08 13:06:51'),(2,2,NULL,1,'ROLE_UPDATED','Ruolo dell\'utente ID 2 modificato da cittadino a ministro.','2026-07-08 13:07:00'),(3,1,NULL,1,'RESOURCE_UPDATED','Risorse aggiornate dopo il comunicato ufficiale: monete +3, cultura +2.','2026-07-08 13:11:19'),(4,1,NULL,1,'RESOURCE_UPDATED','Risorse aggiornate dopo il comunicato ufficiale: monete +3, cultura +2.','2026-07-08 13:11:30'),(5,1,1,2,'LAW_PROPOSED','Nuova legge proposta: Legge sulla pausa istituzionale','2026-07-08 13:12:21'),(6,1,1,NULL,'RESOURCE_UPDATED','Risorse aggiornate dopo l\'approvazione della legge: monete +20, cultura +10, energia +5.','2026-07-08 13:13:53'),(7,1,1,2,'LAW_APPROVED','Legge \"Legge sulla pausa istituzionale\" approvata con voti favorevoli=2 e voti contrari=0.','2026-07-08 13:13:53'),(8,1,2,2,'LAW_PROPOSED','Nuova legge proposta: Legge sulle tazze obbligatorie','2026-07-08 13:14:09'),(9,1,2,NULL,'RESOURCE_UPDATED','Risorse aggiornate dopo la bocciatura della legge: monete -5, energia -2.','2026-07-08 13:14:57'),(10,1,2,1,'LAW_REJECTED','Legge \"Legge sulle tazze obbligatorie\" respinta con voti favorevoli=1 e voti contrari=2.','2026-07-08 13:14:57'),(11,1,1,NULL,'RESOURCE_UPDATED','Risorse aggiornate dopo l\'abrogazione della legge: monete -10, cultura -5.','2026-07-08 13:15:16'),(12,1,1,1,'LAW_REPEALED','Legge \"Legge sulla pausa istituzionale\" abrogata.','2026-07-08 13:15:16');
/*!40000 ALTER TABLE `decision_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `nation_resources`
--

LOCK TABLES `nation_resources` WRITE;
/*!40000 ALTER TABLE `nation_resources` DISABLE KEYS */;
INSERT INTO `nation_resources` VALUES (1,111,9,3),(2,100,0,0);
/*!40000 ALTER TABLE `nation_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `national_newspapers`
--

LOCK TABLES `national_newspapers` WRITE;
/*!40000 ALTER TABLE `national_newspapers` DISABLE KEYS */;
INSERT INTO `national_newspapers` VALUES (1,1,1,'2026-07','Giornale Nazionale di Repubblica del Caffè — 2026-07','Edizione nazionale del periodo 2026-07 per la micro-nazione Repubblica del Caffè. La comunità conta 3 cittadini, con 1 ministri attivi. La nazione mostra una struttura civica già avviata e una base politica osservabile.','Il quadro politico mostra una fase di confronto intenso: le leggi respinte superano quelle approvate.','Le risorse simboliche registrano 111 monete, 9 punti cultura e 3 punti energia. Il livello energetico è basso e potrebbe richiedere nuove decisioni favorevoli.','L\'attività legislativa comprende 2 leggi totali: 0 proposte, 0 approvate, 1 respinte e 1 abrogate. Ultime leggi registrate: \"Legge sulle tazze obbligatorie\" (Respinta); \"Legge sulla pausa istituzionale\" (Abrogata).','La comunicazione istituzionale comprende 2 comunicati complessivi. Ultimi comunicati: \"Avvio delle consultazioni legislative\"; \"Apertura ufficiale della Repubblica del Caffè\".','Lo storico decisionale contiene 11 eventi. Eventi più recenti: Risorse aggiornate; Legge abrogata; Risorse aggiornate; Legge respinta; Legge proposta.','2026-07-08 13:15:31');
/*!40000 ALTER TABLE `national_newspapers` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-08 15:29:04
