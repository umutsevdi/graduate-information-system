create or replace function createAnnouncementControl()
    returns trigger as
$body$
begin
    if (
            new."from" not in (select "id" from "user")
        ) then
        raise exception 'exception: given user is not found';
    end if;
    return new;
end;
$body$
    language plpgsql volatile;

create or replace function deleteAnnouncementControl()
    returns trigger as
$body$
begin
    if (
            new."from" not in (select "id" from "user")
        ) then
        raise exception 'exception: given user is not found';
    else
        delete from "likes" where likes."from"=old."from";
    end if;
    return new;
end;
$body$
    language plpgsql volatile;


create trigger "create_announcement"
    before insert or update
    on "announcement"
    for each row
execute procedure createAnnouncementControl();


create trigger "delete_announcement"
    before delete
    on "announcement"
    for each row
execute procedure deleteAnnouncementControl();