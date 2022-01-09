/*
 user functions
 createcheck() : checks whether given input is a valid user entry
 */

create or replace function createUserControl()
    returns trigger as
$body$
begin
    if (
                new.gender not in ('Male', 'Female', 'Other') or
                date_gt(new.dob, current_date) or
                date_gt(new.g_year, current_date) or
                new.profession not in (select profession_name from university) or
                new.phone not like '(%%%) %%%%%%%' or
                old.created_at <> new.created_at
        ) then
        raise exception 'exception: invalid arguments while registering % %',new.f_name,new.l_name;
    end if;
    return new;
end;
$body$
    language plpgsql volatile;

create or replace function deleteUserControl()
    returns trigger as
$body$
begin
    raise info 'deleting % %', old.f_name,old.l_name;
    if (old.id in (select "from" from form where "from" = old.id)) then
        raise info '% form were deleted',(select count("from") from form where "from" = old.id);
        delete from form where form."from" = old.id;
    end if;
    if (old.id in (select "from" from announcement where "from" = old.id)) then
        raise info '% announcements were deleted',(select count("from") from announcement where "from" = old.id);
        delete from announcement where announcement."from" = old.id;
    end if;
    if (old.id in (select "from" from job_ad where "from" = old.id)) then
        raise info '% job entries were deleted',(select count("from") from job_ad where "from" = old.id);
        delete from job_ad where job_ad."from" = old.id;
    end if;


    return new;
end;
$body$
    language plpgsql volatile;


create trigger "create_user"
    before insert or update
    on "user"
    for each row
execute procedure createUserControl();


create trigger "delete_user"
    before delete
    on "user"
    for each row
execute procedure deleteUserControl();