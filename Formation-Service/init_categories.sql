-- Script d'initialisation des catégories
INSERT INTO categories (name, description, is_active) VALUES
                                                          ('Design', 'Formations en design graphique, UI/UX, et création visuelle', true),
                                                          ('Cybersecurity', 'Formations en sécurité informatique, ethical hacking, et protection des données', true),
                                                          ('AI', 'Formations en intelligence artificielle, machine learning, et deep learning', true),
                                                          ('Data Science', 'Formations en analyse de données, big data, et visualisation', true),
                                                          ('Web Development', 'Formations en développement web frontend et backend', true),
                                                          ('Mobile Development', 'Formations en développement d''applications mobiles iOS et Android', true),
                                                          ('DevOps', 'Formations en DevOps, CI/CD, et infrastructure cloud', true),
                                                          ('Business', 'Formations en gestion de projet, entrepreneuriat, et marketing digital', true)
    ON CONFLICT (name) DO NOTHING;