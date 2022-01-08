create or replace function createJobControl()
    returns trigger as
$body$
begin
    if (
            new."from" = any (
            select id
            from "user"
            where "user".company IS NULL)

        -- or new."from" not in (select id from "user" where "user".cv_path is null)
        ) then
        raise exception 'exception: invalid arguments while creating a job entry';
    end if;
    return new;
end ;
$body$ language plpgsql volatile;


create or replace function deleteJobControl()
    returns trigger as
$body$
begin
    raise info 'deleting % job apply form for % by %',
        (select count(id) from "form" where old.id = form."to"),
        old.title,old."from";
    delete from form where old.id=form."to";
    return old;
end;
$body$
    language plpgsql volatile;



create trigger "create_job"
    before insert
    on "job_ad"
    for each row
execute procedure createJobControl();


create trigger "delete_job"
    before delete
    on "job_ad"
    for each row
execute procedure deleteJobControl()