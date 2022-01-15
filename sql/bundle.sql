CREATE OR REPLACE VIEW likedPeople AS
SELECT "user".*
FROM "user",
     likes
WHERE likes."from" = "user".id;

do
$$
    declare
        rec record;
    begin
        for rec in select f_name, l_name, profession
                   from "user"
                   where "user".id > 50
                   order by created_at
            loop
                raise notice '% % (%)', rec.f_name, rec.l_name,rec.profession;
            end loop;
    end;
$$