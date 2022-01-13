create or replace function createLikeControl()
    returns trigger as
$body$
begin
    if (
        new."from" not in (select "id" from "user")
        ) then
        raise exception 'exception: given user is not found';

    elsif (exists(select * from likes where likes."from" = new."from" and likes.post_id = new.post_id)
        ) then
        raise exception 'exception: user already liked given post';
    else
        update announcement set "like"="like" + 1 where new.post_id = announcement.id;

    end if;
    return new;
end;
$body$
    language plpgsql volatile;

create or replace function deleteLikeControl()
    returns trigger as
$body$
begin
    update announcement set "like"="like" - 1 where new.post_id = announcement.id;
    return old;
end;
$body$
    language plpgsql volatile;


create trigger "create_like"
    before insert or update
    on "likes"
    for each row
execute procedure createLikeControl();


create trigger "delete_like"
    after delete
    on "likes"
    for each row
execute procedure deleteLikeControl();